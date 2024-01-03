package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspStringLiteral extends AspAtom {
    String value;


    AspStringLiteral(int n) {
        super(n);
    }

    static AspStringLiteral parse(Scanner s) {
        enterParser("string literal");

        AspStringLiteral asl = new AspStringLiteral(s.curLineNum());
        asl.value = s.curToken().stringLit;
        skip(s, stringToken);

        leaveParser("string literal");
        return asl;
    }

    @Override
    public void prettyPrint() {
        prettyWrite('"' + value + '"');
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeStringValue(value);
    }
}
