package aplicacao;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.XadrezException;
import xadrez.XadrezPosicao;

public class Programa {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		PartidaXadrez partidaXadrez = new PartidaXadrez();
		List<PecaXadrez> capturada = new ArrayList<>();
		while(!partidaXadrez.getCheckMate()) {
			try {
				UI.clearScreen();
				UI.printPartida(partidaXadrez, capturada);
				System.out.println();
				System.out.print("Source: ");
				XadrezPosicao source = UI.readXadrezPosicao(sc); 
			
				boolean[][] possivelMoves = partidaXadrez.possiveisMoves(source);
				UI.clearScreen();
				UI.printTabuleiro(partidaXadrez.getPecas(), possivelMoves);
				System.out.println();
				System.out.print("Destino: ");
				XadrezPosicao target = UI.readXadrezPosicao(sc);
				
				PecaXadrez capturadaPeca = partidaXadrez.performXadrezMove(source, target);
				
				if(capturadaPeca != null) {
					capturada.add(capturadaPeca);
				}
				
				if(partidaXadrez.getPromocao() != null) {
					System.out.print("Escolha a promoção (B/C/T/Q): ");
					String tipo = sc.nextLine().toUpperCase();
					while(!tipo.equals("B") && !tipo.equals("N") && !tipo.equals("T") && !tipo.equals("Q")) {
						System.out.print("Opção invalida! Escolha a promoção (B/C/T/Q): ");
						tipo = sc.nextLine().toUpperCase();
					}
					partidaXadrez.replacePromocaoPeca(tipo);
				}
			}
			catch(XadrezException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch(InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		UI.clearScreen();
		UI.printPartida(partidaXadrez, capturada);
	}
}
