# Asp Interpreter

This is a interpreter that supports a subset of Python's syntax written in Java
called Asp, and developed as part of UiO's
[IN2030](https://www.uio.no/studier/emner/matnat/ifi/IN2030/) curriculum.

You can found all the supported tokens in
[TokenKind.java](./no/uio/ifi/asp/scanner/TokenKind.java) file.

This project is built upon the codebase provided by the course, and the source
code can be found
[here](https://www.uio.no/studier/emner/matnat/ifi/IN2030/h23/nedlasting/).


## Example program
```python
n = 1000
primes = [True] * (n+1)

def find_primes ():
   for i1 in range(2,n+1):
      i2 = i1 * i1
      while i2 <= n:
         primes[i2] = False
         i2 = i2 + i1

def format (n, w):
   res = str(n)
   while len(res) < w: res = ' '+res
   return res

def list_primes():
   n_printed = 0
   line_buf = ''
   for i in range(2,n+1):
      if primes[i]:
         if n_printed > 0 and n_printed%10 == 0:
            print(line_buf)
            line_buf = ''
            line_buf = line_buf + format(i,4)
            n_printed = n_printed + 1
            print(line_buf)

find_primes()
list_primes()
```

This example program is written in Asp, which supports a subset of Python's
syntax, so it is compatible with both the Asp interpreter and standard Python
interpreter.

## Usage
This project was developed using `java-17`. It might be compatible with certain
lower versions of `java`, though `java-17` is recommended. You will also need to
have `ant` build tool installed on your machine for building.

To build the project:
``` shell
ant jar
```

To run the interpreter with a .asp file:
``` shell
java -jar asp.jar <file.asp>
```

The Asp interpreter also accepts flags to test different parts of the
interpreter, such as scanning, parsing, and evaluating expressions

``` shell
java -jar -testscanner <file.asp> # scan tokens from file
java -jar -testparser <file.asp> # read syntax tree of file
java -jar -testexpr <file.asp> # evaluate expressions to their atomic value
```

In the `test/` folder, you can find various Asp programs to run tests on the
interpreter.

# Acknowledgments

Special thanks to Dag Langmyhr and Ragnhild Kobro Runde for your expertise and
dedication throughout the
[IN2030](https://www.uio.no/studier/emner/matnat/ifi/IN2030/) course at UiO. The
well-organized material, detailed compendium, and your extensive knowledge have
not only made this project a reality but also a truly enriching learning
experience. Your readiness to offer help and answer our questions created a
supportive and engaging atmosphere. This project reflects the high quality of
teaching and mentorship you have provided.
