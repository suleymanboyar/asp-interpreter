package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import java.util.Arrays;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspSuite extends AspSyntax {
    //  NOTE 2023-10-09: If stmts is length of 1, it represetn a small stmt
    //  list, else a list of small stmt.
    ArrayList<AspStmt> stmts = new ArrayList<>();
    boolean isSmallStmtList = false;

    AspSuite(int n){
        super(n);
    }

    static AspSuite parse(Scanner s) {
        enterParser("suite");

        AspSuite as = new AspSuite(s.curLineNum());
        if (s.curToken().kind != newLineToken) {
            as.isSmallStmtList = true;
            as.stmts.add(AspSmallStmtList.parse(s));
        }
        else{
            skip(s, newLineToken);
            skip(s, indentToken);
            while (true) {
                as.stmts.add(AspStmt.parse(s));
                if (s.curToken().kind == dedentToken) break;
            }
            skip(s, dedentToken);
        }

        leaveParser("suite");
        return as;
    }

    @Override
    public void prettyPrint() {
        if (!isSmallStmtList){
            prettyWriteLn();
            prettyIndent();
        }

        // NOTE: stmts will only have one stmt if isSmallStmtList is true
        for (AspStmt stmt : stmts)
            stmt.prettyPrint();

        if(!isSmallStmtList)
            prettyDedent();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        for (AspStmt stmt : stmts) {
            stmt.eval(curScope);
        }
        return null;
    }
}
