package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspListDisplay extends AspAtom {
    ArrayList<AspExpr> exprs = new ArrayList<>();

    AspListDisplay(int n) {
        super(n);
    }

    static AspListDisplay parse(Scanner s) {
        enterParser("list display");

        AspListDisplay ald = new AspListDisplay(s.curLineNum());
        skip(s, leftBracketToken);

        // Check if list is not empty
        if (s.curToken().kind != rightBracketToken){
            while (true){
                ald.exprs.add(AspExpr.parse(s));
                if (s.curToken().kind == rightBracketToken) break;
                skip(s, commaToken);
            }
        }
        skip(s, rightBracketToken);

        leaveParser("list display");
        return ald;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("[");
        int nPrinted = 0;
        for (AspExpr ae : exprs) {
            if (nPrinted > 0)
                prettyWrite(", ");
            ae.prettyPrint();
            ++nPrinted;
        }
        prettyWrite("]");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        ArrayList<RuntimeValue> list = new ArrayList<>();
        for (AspExpr expr : exprs) {
            list.add(expr.eval(curScope));
        }
        return new RuntimeListValue(list);
    }
}
