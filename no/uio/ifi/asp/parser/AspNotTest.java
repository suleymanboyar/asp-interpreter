package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspNotTest extends AspSyntax {
    boolean withNot = false;
    AspComparison comparison;
    AspNotTest(int n) {
        super(n);
    }

    public static AspNotTest parse(Scanner s) {
        enterParser("not test");

        AspNotTest ant = new AspNotTest(s.curLineNum());
        if (s.curToken().kind == notToken){
            ant.withNot = s.curToken().kind == notToken;
            skip(s, notToken);
        }
        ant.comparison = AspComparison.parse(s);

        leaveParser("not test");
        return ant;
    }

    @Override
    public void prettyPrint() {
        if (withNot)
            prettyWrite("not ");
        comparison.prettyPrint();
    }

    // Hentet fra IN2030 2023 forelesning
    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = comparison.eval(curScope);
        if (withNot) {
            v = v.evalNot(this);
        }
        return v;
    }
}
