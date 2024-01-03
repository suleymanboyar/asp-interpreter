package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspTerm extends AspSyntax{
    ArrayList<AspFactor> factors = new ArrayList<>();
    ArrayList<AspTermOpr> termOprs = new ArrayList<>();

    AspTerm(int n){
        super(n);
    }

    public static AspTerm parse(Scanner s){
        enterParser("term");

        AspTerm at = new AspTerm(s.curLineNum());
        while (true){
            at.factors.add(AspFactor.parse(s));
            if (!s.isTermOpr()) break;
            at.termOprs.add(AspTermOpr.parse(s));
        }

        leaveParser("term");
        return at;
    }

    @Override
    public void prettyPrint() {
        int nPrinted = 0;
        for (int i = 0; i < factors.size(); i++) {
            if (nPrinted > 0)
                termOprs.get(i-1).prettyPrint();
            factors.get(i).prettyPrint();
            nPrinted++;
        }
    }

    // Hentet fra IN2030 2023 forelesning
    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = factors.get(0).eval(curScope);
        for (int i = 1; i < factors.size(); i++) {
            AspFactor factor = factors.get(i);
            TokenKind k = termOprs.get(i-1).token.kind;
            switch (k) {
            case minusToken:
                v = v.evalSubtract(factor.eval(curScope), this); break;
            case plusToken:
                v = v.evalAdd(factor.eval(curScope), this); break;
            default:
                Main.panic("Illegal term operator: " + k + "!");
            }
        }
        return v;
    }

}
