package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspArguments extends AspPrimarySuffix {
    ArrayList<AspExpr> exprs = new ArrayList<>();

    AspArguments(int n) {
        super(n);
    }


    static AspArguments parse(Scanner s) {
        enterParser("arguments");

        AspArguments aa = new AspArguments(s.curLineNum());
        skip(s, leftParToken);

        // Check if arguments list is not empty
        if (s.curToken().kind != rightParToken){
            while (true){
                aa.exprs.add(AspExpr.parse(s));
                if (s.curToken().kind == rightParToken) break;
                skip(s, commaToken);
            }
        }

        skip(s, rightParToken);

        leaveParser("arguments");
        return aa;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("(");
        int nPrinted = 0;
        for (AspExpr expr : exprs){
            if (nPrinted > 0)
                prettyWrite(", ");
            expr.prettyPrint();
            nPrinted++;
        }
        prettyWrite(")");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        ArrayList<RuntimeValue> list = new ArrayList<>();
        for (AspExpr expr : exprs)
            list.add(expr.eval(curScope));
        return new RuntimeListValue(list);
    }
}
