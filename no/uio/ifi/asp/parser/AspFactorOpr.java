package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspFactorOpr extends AspSyntax{
    Token token;
    static TokenKind[] factorOprTokenKinds = {astToken, slashToken,
                                              percentToken, doubleSlashToken};

    AspFactorOpr(int n){
        super(n);
    }

    public static AspFactorOpr parse(Scanner s){
        enterParser("factor opr");

        AspFactorOpr afo = new AspFactorOpr(s.curLineNum());
        afo.token = s.curToken();
        test(s, factorOprTokenKinds);
        skip(s, afo.token.kind);

        leaveParser("factor opr");
        return afo;
    }

    @Override
    public void prettyPrint() {
        prettyWrite(" " + token + " ");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // NOTE 2023-11-26: factor opr is a terminal symbol and its eval should
        // not be used
        assert false: "factor opr eval() is unreachable!";
        return null;
    }

}
