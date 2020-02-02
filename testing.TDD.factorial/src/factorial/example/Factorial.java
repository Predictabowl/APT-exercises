package factorial.example;

import java.util.stream.IntStream;

public class Factorial {

	/*
	public int compute(int n) {
		if (n < 0)
			throw new IllegalArgumentException("Negative input: " + n);
		int result = 1;
		for (int i = 2; i <= n; i++) {
			result = result * i;
		}
		return result;
	}
*/
	
	public int compute(int n) {
		if (n < 0)
			throw new IllegalArgumentException("Negative input: " + n);
		return IntStream.rangeClosed(2, n).reduce(1, (x,y) -> x*y);
	}

}

/* A prescindere dai test utilizzati ci sarà sempre almeno un mutante sopravvisuto:
 * Ad esempio cambiando il letterale dentro rangeClosed da 2 a 1 si ottiene un mutante
 * che soddisfa comunque tutti i test. Questo perché tale mutante comunque implementa
 * correttamente il fattoriale, semplicemente eseguendo un'operazione superflua aggiuntiva
 * moltiplicando per 1, che volendo essere ligi alla definizione di fattoriale è
 * formalmente corretto.
 * Ritengo che non abbia senso scrivere test per impedire a tale mutante di sopravvivere.
 */
