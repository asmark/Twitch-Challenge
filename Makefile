JFLAGS = -g -sourcepath src
JC = javac
WORDS = $(PWD)/words.txt
.SUFFIXES:.java .class

CLASSDIR = class

.java.class:
	$(JC) $(JFLAGS) $*.java -d $(CLASSDIR)

CLASSES = src/spellcheck/Main.java
MAIN = spellcheck/Main

default: classes

classes: dirs $(CLASSES:.java=.class)

run: classes
	@cd $(CLASSDIR) && java $(MAIN) words.txt

dirs:
	@mkdir -p $(CLASSDIR)
	@cp $(WORDS) $(CLASSDIR)/words.txt

clean:
	$(RM) -r $(CLASSDIR)
