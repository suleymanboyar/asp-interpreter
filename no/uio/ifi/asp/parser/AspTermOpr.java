package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspTermOpr extends AspSyntax{
    Token token = null;         // + or -

    AspTermOpr(int n){
        super(n);
    }

    public static AspTermOpr parse(Scanner s){
        enterParser("term opr");

        AspTermOpr aco = new AspTermOpr(s.curLineNum());
        aco.token = s.curToken();
        test(s, plusToken, minusToken);
        skip(s, aco.token.kind);

        leaveParser("term opr");
        return aco;
    }

    @Override
    public void prettyPrint() {
        prettyWrite(" " + token + " ");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // NOTE 2023-11-26: term opr is a terminal symbol and its eval should
        // not be used
        return null;
    }

}
