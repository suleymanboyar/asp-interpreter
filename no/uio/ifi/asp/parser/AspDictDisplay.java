package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.HashMap;

class AspDictDisplay extends AspAtom {
    ArrayList<AspStringLiteral> strings = new ArrayList<>();
    ArrayList<AspExpr> exprs = new ArrayList<>();

    AspDictDisplay(int n) {
        super(n);
    }

    static AspDictDisplay parse(Scanner s) {
        enterParser("dict display");

        AspDictDisplay ad = new AspDictDisplay(s.curLineNum());
        skip(s, leftBraceToken);

        // Check if dict is not empty
        if (s.curToken().kind != rightBraceToken){
            while (true){
                test(s, stringToken);
                ad.strings.add(AspStringLiteral.parse(s));
                skip(s, colonToken);
                ad.exprs.add(AspExpr.parse(s));
                if (s.curToken().kind == rightBraceToken) break;
                skip(s, commaToken);
            }
        }
        skip(s, rightBraceToken);

        leaveParser("dict display");
        return ad;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("{");
        int nPrinted = 0;
        for (int i = 0; i < strings.size(); i++) {
            AspStringLiteral string = strings.get(i);
            AspExpr expr = exprs.get(i);

            if (nPrinted > 0)
                prettyWrite(", ");
            string.prettyPrint();
            prettyWrite(":");
            expr.prettyPrint();
            nPrinted++;
        }
        prettyWrite("}");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        HashMap<String, RuntimeValue> dict = new HashMap<>();
        for (int i = 0; i < strings.size(); i++) {
            String key = strings.get(i).eval(curScope).toString();
            RuntimeValue value = exprs.get(i).eval(curScope);
            dict.put(key, value);
        }
        return new RuntimeDictValue(dict);
    }
}
