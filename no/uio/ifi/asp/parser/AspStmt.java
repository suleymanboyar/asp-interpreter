// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

abstract class AspStmt extends AspSyntax {
    AspStmt(int n) {
        super(n);
    }

    static AspStmt parse(Scanner s) {
        enterParser("stmt");
        AspStmt as = null;
        TokenKind cur = s.curToken().kind;
        if (cur == ifToken || cur == forToken ||
            cur == whileToken || cur == defToken) {
            as = AspCompoundStmt.parse(s);
        }
        else {
            as = AspSmallStmtList.parse(s);
        }
        leaveParser("stmt");
        return as;
    }
}
