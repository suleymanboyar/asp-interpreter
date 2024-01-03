package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspIfStmt extends AspCompoundStmt {
    ArrayList<AspExpr> tests = new ArrayList<>();
    ArrayList<AspSuite> bodies = new ArrayList<>();

    AspSuite elseBody = null;

    AspIfStmt(int n){
        super(n);
    }

    static AspIfStmt parse(Scanner s) {
        enterParser("if stmt");

        AspIfStmt afd = new AspIfStmt(s.curLineNum());
        skip(s, ifToken);
        while (true){
            afd.tests.add(AspExpr.parse(s));
            skip(s, colonToken);
            afd.bodies.add(AspSuite.parse(s));

            if (s.curToken().kind != elifToken)
                break;
            else
                skip(s, elifToken);
        }

        if (s.curToken().kind == elseToken) {
            skip(s, elseToken);
            skip(s, colonToken);
            afd.elseBody = AspSuite.parse(s);
        }

        leaveParser("if stmt");
        return afd;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("if ");
        int nPrinted = 0;
        for (int i = 0; i < tests.size(); i++) {
            if (nPrinted > 0) {
                prettyWrite("elif ");
            }
            tests.get(i).prettyPrint();
            prettyWrite(": ");
            bodies.get(i).prettyPrint();
            nPrinted++;
        }
        if (elseBody != null) {
            prettyWrite("else: ");
            elseBody.prettyPrint();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        for (int i = 0; i < tests.size(); i++) {
            RuntimeValue t = tests.get(i).eval(curScope);
            if (t.getBoolValue("if test", this)){
                trace("if True alt #" + (i+1) + ": ...");
                bodies.get(i).eval(curScope);
                return null;
            }
        }

        if (elseBody != null){
            trace("else: ...");
            elseBody.eval(curScope);
        }

        return null;
    }
}
