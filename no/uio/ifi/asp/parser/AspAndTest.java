package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspAndTest extends AspSyntax {
    ArrayList<AspNotTest> notTests = new ArrayList<>();

    AspAndTest(int n) {
        super(n);
    }

    public static AspAndTest parse(Scanner s) {
        enterParser("and test");

        // Hentet fra IN2030 2022 forelesning
        AspAndTest aat = new AspAndTest(s.curLineNum());
        while (true) {
            aat.notTests.add(AspNotTest.parse(s));
            if (s.curToken().kind != andToken) break;
            skip(s, andToken);
        }

        leaveParser("and test");
        return aat;
    }

    // Hentet fra IN2030 2023 forelesning
    @Override
    public void prettyPrint() {
        int nPrinted = 0;
        for (AspNotTest ant: notTests) {
            if (nPrinted > 0)
                prettyWrite(" and ");
            ant.prettyPrint();
            ++nPrinted;
        }
    }

    // Hentet fra IN2030 2023 forelesning
    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = notTests.get(0).eval(curScope);
        for (int i = 1; i < notTests.size(); ++i) {
            if (! v.getBoolValue("and operand",this))
                return v;
            v = notTests.get(i).eval(curScope);
        }
        return v;
    }
}
