# compiler and compiler flag variables
JFLAGS = -g
JC = javac

# Clear any default targets for building .class files from .java files
.SUFFIXES: .java .class

# Target entry for creating .class files from .java files
.java.class:
	$(JC) $(JFLAGS) $*.java

# Java CLASSES
CLASSES = \
	logQueryClient.java \
	logQueryClientThread.java \
	logQueryServer.java \
	logQueryServerThread.java \
	serverProperties.java \
	logFormatter.java \
	logGenerator.java

# Default make target entry
default: classes

# Suffix Replacement within a macro:
# $(name:string1=string2)
classes: $(CLASSES:.java=.class)

# RM is a predefined macro in make (RM = rm -f)
clean:
	$(RM) *.class

# Run the unit test
#
unittest: default
	./unitTest.sh
