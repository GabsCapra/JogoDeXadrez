package xadrez.pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Color;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;

public class Rei extends PecaXadrez {

	private PartidaXadrez partidaXadrez;

	public Rei(Tabuleiro tabuleiro, Color color, PartidaXadrez partidaXadrez) {
		super(tabuleiro, color);
		this.partidaXadrez = partidaXadrez;
	}

	@Override
	public String toString() {
		return "R";
	}

	private boolean canMove(Posicao posicao) {
		PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
		return p == null || p.getCor() != getCor();
	}

	private boolean testTorreRoque(Posicao posicao) {
		PecaXadrez p = (PecaXadrez) getTabuleiro().peca(posicao);
		return p != null && p instanceof Torre && p.getCor() == getCor() && p.getMoveCount() == 0;
	}

	@Override
	public boolean[][] possiveisMove() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

		Posicao p = new Posicao(0, 0);

		// cima
		p.setValues(posicao.getLinha() - 1, posicao.getColuna());
		if (getTabuleiro().posicaoExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// baixo
		p.setValues(posicao.getLinha() + 1, posicao.getColuna());
		if (getTabuleiro().posicaoExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// esquerda
		p.setValues(posicao.getLinha(), posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// direita
		p.setValues(posicao.getLinha(), posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// noroeste
		p.setValues(posicao.getLinha() - 1, posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// nordeste
		p.setValues(posicao.getLinha() - 1, posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// suldoeste
		p.setValues(posicao.getLinha() + 1, posicao.getColuna() - 1);
		if (getTabuleiro().posicaoExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// suldoeste
		p.setValues(posicao.getLinha() + 1, posicao.getColuna() + 1);
		if (getTabuleiro().posicaoExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// especial movimento torre e rei
		if (getMoveCount() == 0 && !partidaXadrez.getCheck()) {
			// roque pequeno, do lado do rei
			Posicao posT1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 3);
			if (testTorreRoque(posT1)) {
				Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() + 2);
				if (getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null) {
					mat[posicao.getLinha()][posicao.getColuna() + 2] = true;
				}
			}
			// roque grande, do lado da rainha
			Posicao posT2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 4);
			if (testTorreRoque(posT2)) {
				Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 2);
				Posicao p3 = new Posicao(posicao.getLinha(), posicao.getColuna() - 3);
				if (getTabuleiro().peca(p1) == null && getTabuleiro().peca(p2) == null
						&& getTabuleiro().peca(p3) == null) {
					mat[posicao.getLinha()][posicao.getColuna() - 2] = true;
				}
			}
		}

		return mat;
	}
}