package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


abstract class AspPrimarySuffix extends AspSyntax {
    AspPrimarySuffix(int n) {
        super(n);
    }


    static AspPrimarySuffix parse(Scanner s) {
        enterParser("primary suffix");

        AspPrimarySuffix aps = null;
        TokenKind cur = s.curToken().kind;
        if(cur == leftParToken){
            aps = AspArguments.parse(s);
        } else if (cur == leftBracketToken) {
            aps = AspSubscription.parse(s);
        } else {
            test(s, leftParToken, leftBracketToken);
        }

        leaveParser("primary suffix");
        return aps;
    }
}
