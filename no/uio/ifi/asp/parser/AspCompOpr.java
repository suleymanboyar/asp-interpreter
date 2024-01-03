package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspCompOpr extends AspSyntax{
    Token token;
    static TokenKind[] compOprTokenKinds = {greaterToken,
                                            lessToken,
                                            doubleEqualToken,
                                            greaterEqualToken,
                                            lessEqualToken,
                                            notEqualToken};

    AspCompOpr(int n){
        super(n);
    }

    public static AspCompOpr parse(Scanner s){
        enterParser("comp opr");

        AspCompOpr ac = new AspCompOpr(s.curLineNum());
        ac.token = s.curToken();
        test(s, compOprTokenKinds);
        skip(s, ac.token.kind);

        leaveParser("comp opr");
        return ac;
    }

    @Override
    public void prettyPrint() {
        prettyWrite(" " + token + " ");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // NOTE 2023-11-26: comp opr is a terminal symbol and its eval should
        // not be used
        return null;
    }

}
