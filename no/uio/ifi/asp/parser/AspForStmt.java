package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

class AspForStmt extends AspCompoundStmt {
    AspName name;
    AspExpr test;
    AspSuite body;

    AspForStmt(int n){
        super(n);
    }

    static AspForStmt parse(Scanner s) {
        enterParser("for stmt");

        AspForStmt afs = new AspForStmt(s.curLineNum());
        skip(s, forToken);
        afs.name = AspName.parse(s);
        skip(s, inToken);
        afs.test = AspExpr.parse(s);
        skip(s, colonToken);
        afs.body = AspSuite.parse(s);

        leaveParser("for stmt");
        return afs;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("for ");
        name.prettyPrint();
        prettyWrite(" in ");
        test.prettyPrint();
        prettyWrite(": ");
        body.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue expr = test.eval(curScope);
        if (!(expr instanceof RuntimeListValue))
            RuntimeValue.runtimeError("For loop range is not a list!", this);

        ArrayList<RuntimeValue> list = expr.getListValue("for loop list", this);

        int i = 1;
        for (RuntimeValue element : list ) {
            String id = name.toString();
            curScope.assign(id, element);
            trace("for #" + (i++) + ": " + id + " = " + element.showInfo());
            body.eval(curScope);
        }

        return null;
    }
}
