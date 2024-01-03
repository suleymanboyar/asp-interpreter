package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspName extends AspAtom {
    Token token;
    AspName(int n) {
        super(n);
    }

    static AspName parse(Scanner s) {
        enterParser("name");

        AspName an = new AspName(s.curLineNum());
        an.token = s.curToken();
        skip(s, nameToken);

        leaveParser("name");
        return an;
    }

    @Override
    public void prettyPrint() {
        prettyWrite(token.name);
    }

    @Override
    public String toString() {
        return token.name;
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return curScope.find(token.name, this);
    }


}
