package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

// class Factor {
//     AspFactorPrefix prefix;
//     AspPrimary primary;
//
//     Factor(AspFactorPrefix prefix, AspPrimary primary){
//         this.prefix = prefix;
//         this.primary = primary;
//     }
// }

public class AspFactor extends AspSyntax{
    ArrayList<AspFactorOpr> factorOprs = new ArrayList<>();
    ArrayList<AspFactorPrefix> factorPrefixes = new ArrayList<>();
    ArrayList<AspPrimary> primaries = new ArrayList<>();
    // ArrayList<Factor> factor = new ArrayList<>();

    AspFactor(int n){
        super(n);
    }

    public static AspFactor parse(Scanner s){
        enterParser("factor");
        AspFactor af = new AspFactor(s.curLineNum());
        while (true){
            // AspFactorPrefix prefix = null;

            if (s.isFactorPrefix())
                af.factorPrefixes.add(AspFactorPrefix.parse(s));
                // prefix = AspFactorPrefix.parse(s);
            else
                af.factorPrefixes.add(null);

            af.primaries.add(AspPrimary.parse(s));
            // AspPrimary primary = AspPrimary.parse(s);
            // af.factor.add(new Factor(prefix, primary));

            if (!s.isFactorOpr()) break;
            af.factorOprs.add(AspFactorOpr.parse(s));
        }

        leaveParser("factor");
        return af;
    }

    @Override
    public void prettyPrint() {
        int nPrinted = 0;
        for (int i = 0; i < primaries.size(); i++) {
            AspFactorPrefix factorPrefix = factorPrefixes.get(i);
            AspPrimary primary = primaries.get(i);
            if (nPrinted > 0){
                factorOprs.get(i-1).prettyPrint();
            }
            if (factorPrefix != null) {
                factorPrefix.prettyPrint();
            }
            primary.prettyPrint();
            nPrinted++;
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = primaries.get(0).eval(curScope);
        if (factorPrefixes.get(0) != null){
            if (factorPrefixes.get(0).token.kind == plusToken)
                v = v.evalPositive(this);
            else
                v = v.evalNegate(this);
        }

        for (int i = 1; i < primaries.size(); i++) {
            AspPrimary primary = primaries.get(i);
            AspFactorPrefix suffix = factorPrefixes.get(i);
            RuntimeValue curEval = primary.eval(curScope);
            if (suffix != null){
                if (suffix.token.kind == plusToken)
                    curEval = curEval.evalPositive(this);
                else
                    curEval = curEval.evalNegate(this);
            }

            TokenKind k = factorOprs.get(i-1).token.kind;
            switch (k) {
            case astToken:
                v = v.evalMultiply(curEval, this); break;
            case slashToken:
                v = v.evalDivide(curEval, this); break;
            case percentToken:
                v = v.evalModulo(curEval, this); break;
            case doubleSlashToken:
                v = v.evalIntDivide(curEval, this); break;
            default:
                Main.panic("Illegal term operator: " + k + "!");
            }
        }

        return v;
    }

}
