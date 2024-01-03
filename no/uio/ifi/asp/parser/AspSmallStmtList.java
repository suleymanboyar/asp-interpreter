package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspSmallStmtList extends AspStmt {
    ArrayList<AspSmallStmt> smallStmts = new ArrayList<>();

    AspSmallStmtList(int n) {
        super(n);
    }

    static AspSmallStmtList parse(Scanner s) {
        enterParser("small stmt list");

        AspSmallStmtList assl = new AspSmallStmtList(s.curLineNum());
        // Get the first small stmt
        assl.smallStmts.add(AspSmallStmt.parse(s));
        // Get the rest of small stmt where each is devided by a semicolon
        while (s.curToken().kind != newLineToken){
            skip (s, semicolonToken);
            if (s.curToken().kind == newLineToken)
                break;
            assl.smallStmts.add(AspSmallStmt.parse(s));
        }

        skip(s, newLineToken);

        leaveParser("small stmt list");
        return assl;
    }

    @Override
    public void prettyPrint() {
        int nPrinted = 0;
        for (AspSmallStmt smallStmt : smallStmts){
            if (nPrinted > 0)
                prettyWrite("; ");
            smallStmt.prettyPrint();
            nPrinted++;
        }
        prettyWriteLn();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        for (AspSmallStmt smallStmt : smallStmts){
            smallStmt.eval(curScope);
        }

        return null;
    }
}
