package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Bispo;
import xadrez.pecas.Cavalo;
import xadrez.pecas.Peão;
import xadrez.pecas.QueenRainha;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {

	private int turno;
	private Color jogadorAtual;
	private Tabuleiro tabuleiro;
	private boolean check;
	private boolean checkMate;
	private PecaXadrez enPassantVulneravel;
	private PecaXadrez promocao;

	private List<Peca> pecasSobreTabuleiro = new ArrayList<>();
	private List<Peca> pecasCapturada = new ArrayList<>();

	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		turno = 1;
		jogadorAtual = Color.WHITE;
		initialSetup();
	}

	public int getTurno() {
		return turno;
	}

	public Color getJogadorAtual() {
		return jogadorAtual;
	}

	public boolean getCheck() {
		return check;
	}

	public boolean getCheckMate() {
		return checkMate;
	}

	public PecaXadrez getEnPassantVulneravel() {
		return enPassantVulneravel;
	}
	
	public PecaXadrez getPromocao() {
		return promocao;
	}

	public PecaXadrez[][] getPecas() {
		PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i = 0; i < tabuleiro.getLinhas(); i++) {
			for (int j = 0; j < tabuleiro.getColunas(); j++) {
				mat[i][j] = (PecaXadrez) tabuleiro.peca(i, j);
			}
		}
		return mat;
	}

	public boolean[][] possiveisMoves(XadrezPosicao sourcePosicao) {
		Posicao posicao = sourcePosicao.toPosicao();
		validateSourcePosicao(posicao);
		return tabuleiro.peca(posicao).possiveisMove();
	}

	public PecaXadrez performXadrezMove(XadrezPosicao sourcePosicao, XadrezPosicao targetPosicao) {
		Posicao source = sourcePosicao.toPosicao();
		Posicao target = targetPosicao.toPosicao();
		validateSourcePosicao(source);
		validateTargetPosicao(source, target);
		Peca capturadaPeca = makeMove(source, target);

		if (testCheck(jogadorAtual)) {
			undoMove(source, target, capturadaPeca);
			throw new XadrezException("Vc não pode se por check");
		}

		PecaXadrez movedPeca = (PecaXadrez) tabuleiro.peca(target);
		
		//promocao
		promocao = null;
		if(movedPeca instanceof Peão) {
			if(movedPeca.getCor() == Color.WHITE && target.getLinha() == 0 
					|| movedPeca.getCor() == Color.BLACK && target.getLinha() == 7) {
				promocao = (PecaXadrez)tabuleiro.peca(target);
				promocao = replacePromocaoPeca ("Q");
			}
		}

		check = (testCheck(oponente(jogadorAtual))) ? true : false;

		if (testCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		} else {
			nextTurno();
		}
		// #specialmove en passant
		if (movedPeca instanceof Peão
				&& (target.getLinha() == source.getLinha() - 2 || target.getLinha() == source.getLinha() + 2)) {
			enPassantVulneravel = movedPeca;
		} else {
			enPassantVulneravel = null;
		}

		return (PecaXadrez) capturadaPeca;
	}
	
	public PecaXadrez replacePromocaoPeca(String tipo) {
		if(promocao == null) {
			throw new IllegalStateException("Sem peça para ser promovida");
		}
		if(!tipo.equals("B") && !tipo.equals("N") && !tipo.equals("T") && !tipo.equals("Q")) {
			return promocao;
		}
		Posicao pos = promocao.getXadrezPosicao().toPosicao();
		Peca p = tabuleiro.removePeca(pos);
		pecasSobreTabuleiro.remove(p);
		
		PecaXadrez newPeca = newPeca(tipo, promocao.getCor());
		tabuleiro.localPeca(newPeca, pos);
		pecasSobreTabuleiro.add(newPeca);
		
		return newPeca;
		
	}
	
	private PecaXadrez newPeca(String tipo, Color cor) {
		if(tipo.equals("B")) return new Bispo(tabuleiro, cor);
		if(tipo.equals("C")) return new Cavalo(tabuleiro, cor);
		if(tipo.equals("Q")) return new QueenRainha(tabuleiro, cor);
		return new Torre(tabuleiro, cor);
	}

	private Peca makeMove(Posicao source, Posicao target) {
		PecaXadrez p = (PecaXadrez) tabuleiro.removePeca(source);
		p.increaseMoveCount();
		Peca capturadaPeca = tabuleiro.removePeca(target);
		tabuleiro.localPeca(p, target);

		if (capturadaPeca != null) {
			pecasSobreTabuleiro.remove(capturadaPeca);
			pecasCapturada.add(capturadaPeca);
		}

		// roque pequeno
		if (p instanceof Rei && target.getColuna() == source.getColuna() + 2) {
			Posicao sourceT = new Posicao(source.getLinha(), source.getColuna() + 3);
			Posicao targetT = new Posicao(source.getLinha(), source.getColuna() + 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removePeca(sourceT);
			tabuleiro.localPeca(torre, targetT);
			torre.increaseMoveCount();
		}
		// roque grande
		if (p instanceof Rei && target.getColuna() == source.getColuna() - 2) {
			Posicao sourceT = new Posicao(source.getLinha(), source.getColuna() - 4);
			Posicao targetT = new Posicao(source.getLinha(), source.getColuna() - 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removePeca(sourceT);
			tabuleiro.localPeca(torre, targetT);
			torre.increaseMoveCount();
		}

		// #specialmove en passant
		if (p instanceof Peão) {
			if (source.getColuna() != target.getColuna() && capturadaPeca == null) {
				Posicao peaoPosicao;
				if (p.getCor() == Color.WHITE) {
					peaoPosicao = new Posicao(target.getLinha() + 1, target.getColuna());
				} else {
					peaoPosicao = new Posicao(target.getLinha() - 1, target.getColuna());
				}
				capturadaPeca = tabuleiro.removePeca(peaoPosicao);
				pecasCapturada.add(capturadaPeca);
				pecasSobreTabuleiro.remove(capturadaPeca);
			}
		}

		return capturadaPeca;
	}

	private void undoMove(Posicao source, Posicao target, Peca capturadaPeca) {
		PecaXadrez p = (PecaXadrez) tabuleiro.removePeca(target);
		p.decreaseMoveCount();
		tabuleiro.localPeca(p, source);

		if (capturadaPeca != null) {
			tabuleiro.localPeca(capturadaPeca, target);
			pecasCapturada.remove(capturadaPeca);
			pecasSobreTabuleiro.add(capturadaPeca);
		}
		// roque pequeno
		if (p instanceof Rei && target.getColuna() == source.getColuna() + 2) {
			Posicao sourceT = new Posicao(source.getLinha(), source.getColuna() + 3);
			Posicao targetT = new Posicao(source.getLinha(), source.getColuna() + 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removePeca(targetT);
			tabuleiro.localPeca(torre, sourceT);
			torre.decreaseMoveCount();
		}
		// roque grande
		if (p instanceof Rei && target.getColuna() == source.getColuna() - 2) {
			Posicao sourceT = new Posicao(source.getLinha(), source.getColuna() - 4);
			Posicao targetT = new Posicao(source.getLinha(), source.getColuna() - 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removePeca(targetT);
			tabuleiro.localPeca(torre, sourceT);
			torre.decreaseMoveCount();
		}
		// #specialmove en passant
		if (p instanceof Peão) {
			if (source.getColuna() != target.getColuna() && capturadaPeca == enPassantVulneravel) {
				PecaXadrez peao = (PecaXadrez) tabuleiro.removePeca(target);
				Posicao peaoPosicao;
				if (p.getCor() == Color.WHITE) {
					peaoPosicao = new Posicao(3, target.getColuna());
				} else {
					peaoPosicao = new Posicao(4, target.getColuna());
				}
				tabuleiro.localPeca(peao, peaoPosicao);
			}
		}
	}

	private void validateSourcePosicao(Posicao posicao) {
		if (!tabuleiro.thereIsAPeca(posicao)) {
			throw new XadrezException("Não existe peça na posição de origem");
		}
		if (jogadorAtual != ((PecaXadrez) tabuleiro.peca(posicao)).getCor()) {
			throw new XadrezException("Peça escolhida do outro jogador");
		}
		if (!tabuleiro.peca(posicao).isThereAnyPossivelMove()) {
			throw new XadrezException("Sem movimentos possiveis para a peça escolhida.");
		}
	}

	private void validateTargetPosicao(Posicao source, Posicao target) {
		if (!tabuleiro.peca(source).possiveisMove(target)) {
			throw new XadrezException("sem conseguir");
		}
	}

	private void nextTurno() {
		turno++;
		jogadorAtual = (jogadorAtual == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	private Color oponente(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	private PecaXadrez Rei(Color color) {
		List<Peca> list = pecasSobreTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == color)
				.collect(Collectors.toList());
		for (Peca p : list) {
			if (p instanceof Rei) {
				return (PecaXadrez) p;
			}
		}
		throw new IllegalStateException("Não existe o " + color + "Rei no tabuleiro");
	}

	private boolean testCheck(Color color) {
		Posicao posicaoRei = Rei(color).getXadrezPosicao().toPosicao();
		List<Peca> oponentePecas = pecasSobreTabuleiro.stream()
				.filter(x -> ((PecaXadrez) x).getCor() == oponente(color)).collect(Collectors.toList());
		for (Peca p : oponentePecas) {
			boolean[][] mat = p.possiveisMove();
			if (mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
				return true;
			}
		}
		return false;
	}

	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;
		}
		List<Peca> list = pecasSobreTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == color)
				.collect(Collectors.toList());
		for (Peca p : list) {
			boolean[][] mat = p.possiveisMove();
			for (int i = 0; i < tabuleiro.getLinhas(); i++) {
				for (int j = 0; j < tabuleiro.getColunas(); j++) {
					if (mat[i][j]) {
						Posicao source = ((PecaXadrez) p).getXadrezPosicao().toPosicao();
						Posicao target = new Posicao(i, j);
						Peca capturadaPeca = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturadaPeca);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private void localNewPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.localPeca(peca, new XadrezPosicao(coluna, linha).toPosicao());
		pecasSobreTabuleiro.add(peca);
	}

	private void initialSetup() {
		localNewPeca('a', 1, new Torre(tabuleiro, Color.WHITE));
		localNewPeca('h', 1, new Torre(tabuleiro, Color.WHITE));
		localNewPeca('e', 1, new Rei(tabuleiro, Color.WHITE, this));
		localNewPeca('a', 2, new Peão(tabuleiro, Color.WHITE, this));
		localNewPeca('b', 2, new Peão(tabuleiro, Color.WHITE, this));
		localNewPeca('c', 2, new Peão(tabuleiro, Color.WHITE, this));
		localNewPeca('d', 2, new Peão(tabuleiro, Color.WHITE, this));
		localNewPeca('e', 2, new Peão(tabuleiro, Color.WHITE, this));
		localNewPeca('f', 2, new Peão(tabuleiro, Color.WHITE, this));
		localNewPeca('g', 2, new Peão(tabuleiro, Color.WHITE, this));
		localNewPeca('h', 2, new Peão(tabuleiro, Color.WHITE, this));
		localNewPeca('c', 1, new Bispo(tabuleiro, Color.WHITE));
		localNewPeca('f', 1, new Bispo(tabuleiro, Color.WHITE));
		localNewPeca('b', 1, new Cavalo(tabuleiro, Color.WHITE));
		localNewPeca('g', 1, new Cavalo(tabuleiro, Color.WHITE));
		localNewPeca('d', 1, new QueenRainha(tabuleiro, Color.WHITE));

		localNewPeca('a', 8, new Torre(tabuleiro, Color.BLACK));
		localNewPeca('h', 8, new Torre(tabuleiro, Color.BLACK));
		localNewPeca('e', 8, new Rei(tabuleiro, Color.BLACK, this));
		localNewPeca('a', 7, new Peão(tabuleiro, Color.BLACK, this));
		localNewPeca('b', 7, new Peão(tabuleiro, Color.BLACK, this));
		localNewPeca('c', 7, new Peão(tabuleiro, Color.BLACK, this));
		localNewPeca('d', 7, new Peão(tabuleiro, Color.BLACK, this));
		localNewPeca('e', 7, new Peão(tabuleiro, Color.BLACK, this));
		localNewPeca('f', 7, new Peão(tabuleiro, Color.BLACK, this));
		localNewPeca('g', 7, new Peão(tabuleiro, Color.BLACK, this));
		localNewPeca('h', 7, new Peão(tabuleiro, Color.BLACK, this));
		localNewPeca('c', 8, new Bispo(tabuleiro, Color.BLACK));
		localNewPeca('f', 8, new Bispo(tabuleiro, Color.BLACK));
		localNewPeca('b', 8, new Cavalo(tabuleiro, Color.BLACK));
		localNewPeca('g', 8, new Cavalo(tabuleiro, Color.BLACK));
		localNewPeca('d', 8, new QueenRainha(tabuleiro, Color.BLACK));

	}
}
