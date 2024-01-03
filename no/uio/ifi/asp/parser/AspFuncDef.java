package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspFuncDef extends AspCompoundStmt {
    AspName name;
    ArrayList<AspName> arguments = new ArrayList<>();
    AspSuite body;

    AspFuncDef(int n){
        super(n);
    }

    static AspFuncDef parse(Scanner s) {
        enterParser("func def");

        AspFuncDef afd = new AspFuncDef(s.curLineNum());
        skip(s, defToken);
        afd.name = AspName.parse(s);
        skip(s, leftParToken);
        while (s.curToken().kind != rightParToken) {
            afd.arguments.add(AspName.parse(s));
            if (s.curToken().kind != commaToken) break;
            skip(s, commaToken);
        }
        skip(s, rightParToken); skip(s, colonToken);
        afd.body = AspSuite.parse(s);

        leaveParser("func def");
        return afd;
    }

    public ArrayList<AspName> getArgs() {
        return arguments;
    }

    public String getArgNameAt(int i) {
        return arguments.get(i).toString();
    }

    @Override
    public void prettyPrint() {
        prettyWrite("def ");
        name.prettyPrint();
        prettyWrite(" (");
        int nPrinted = 0;
        for (AspName argument : arguments){
            if (nPrinted > 0)
                prettyWrite(", ");
            argument.prettyPrint();
            nPrinted++;
        }
        prettyWrite("): ");
        body.prettyPrint();
        prettyWriteLn();
    }

    public void evalBody(RuntimeScope curScope) throws RuntimeReturnValue {
        body.eval(curScope);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        curScope.assign(name.toString(), new RuntimeFunc(name.toString(), curScope, this));
        trace("def " + name.toString());
        return null;
    }
}
