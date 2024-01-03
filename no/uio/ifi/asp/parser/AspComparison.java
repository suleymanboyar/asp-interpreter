package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspComparison extends AspSyntax{
    ArrayList<AspCompOpr> compOprs = new ArrayList<>();
    ArrayList<AspTerm> terms = new ArrayList<>();

    AspComparison(int n){
        super(n);
    }

    public static AspComparison parse(Scanner s){
        enterParser("comparison");
        AspComparison ac = new AspComparison(s.curLineNum());
        while (true) {
            ac.terms.add(AspTerm.parse(s));
            if (!s.isCompOpr()) break;
            ac.compOprs.add(AspCompOpr.parse(s));
        }

        leaveParser("comparison");
        return ac;
    }

    @Override
    public void prettyPrint() {
        int nPrinted = 0;
        for (int i = 0; i < terms.size(); i++) {
            if (nPrinted > 0)
                compOprs.get(i-1).prettyPrint();
            terms.get(i).prettyPrint();
            nPrinted++;
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = terms.get(0).eval(curScope);
        RuntimeValue prevV = v;
        for (int i = 1; i < terms.size(); i++) {
            AspTerm term = terms.get(i);
            TokenKind k = compOprs.get(i-1).token.kind;
            switch (k) {
            case lessToken:
                v = prevV.evalLess(term.eval(curScope), this);
                break;
            case greaterToken:
                v = prevV.evalGreater(term.eval(curScope), this);
                break;
            case doubleEqualToken:
                v = prevV.evalEqual(term.eval(curScope), this);
                break;
            case lessEqualToken:
                v = prevV.evalLessEqual(term.eval(curScope), this);
                break;
            case greaterEqualToken:
                v = prevV.evalGreaterEqual(term.eval(curScope), this);
                break;
            case notEqualToken:
                v = prevV.evalNotEqual(term.eval(curScope), this);
                break;
            default:
                Main.panic("Illegal comp operator: " + k + "!");
            }
            prevV = term.eval(curScope);
        }
        return v;
    }

}
