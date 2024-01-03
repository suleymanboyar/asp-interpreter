package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspBooleanLiteral extends AspAtom {
    Token token;
    boolean value;

    AspBooleanLiteral(int n) {
        super(n);
    }

    static AspBooleanLiteral parse(Scanner s) {
        enterParser("boolean literal");

        AspBooleanLiteral abl = new AspBooleanLiteral(s.curLineNum());
        abl.value = s.curToken().kind == trueToken;
        test(s, trueToken, falseToken);
        skip(s, s.curToken().kind);

        leaveParser("boolean literal");
        return abl;
    }

    @Override
    public void prettyPrint() {
        if (value)
            prettyWrite("True");
        else
            prettyWrite("False");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeBoolValue(value);
    }
}
