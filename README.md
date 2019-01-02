# Log-query-system
Distributed log query processing system.

# Compilation
Makefile is provided to build the source files (.java files)
Use the below command to compile the files
- Execution on shell: 'make'
 	Build the class files

# Execution
- Server. Use the below command to initiate the Server

	"java LogQueryServer <port_number> <logfile_path>"

	<port_number>:   TCP port number
	<logfile_path>:  Path where log file resides

	eg. 'java LogQueryServer 1024 /tmp/mylogs/machine.1.log'

- Client. A query can be initiated from any node using the below command

	"java LogQueryClient <grep_arg1> <grep_arg2> ... <grep_argN>"

	<grep_argN> : Nth argument for the grep command (all grep arugments are appliable)

	eg. 'java LogQueryClient -E "Hello World"'

	All available features/flags are available in LogQueryClient as well.

- Generator. Generates logs for common, uncomon and rare patterns
	Logs can be generated using below command

	"java LogGenerator <log_path> <file_size> <machine_id>

	<log_path> : The path that you store the output log file
	<file_size> : The size of log file to be generated
	<machine_id>: The node for which the log is generated. A logical id can
	be assigned for each machine

- Unit Test. Testes the modules/generation of logs according to the patterns
	and a particular definition of request

	The test unit developed can be executed as follows.
	'make unittest' or './LogQueryUnitTest.sh'

	This test unit uses ssh to distribute files into multiple machines.
	Please configure SSH according to the following description (SSH no password for login)
	http://www.linuxproblem.org/art_9.html

# Description of Modules implemented

* Log Query Client. Connect to the servers.
	- Highlights.
		* Servers hard-coded on function addServer(ServerProperty server)
		* Working subnet may vary according to connection.
		* Workers result (Log from workers) fetched on try/catch.

* Log Query Client (Worker Thread). Fetches the query/queries execution based on grep format.
	- Highlights.
		* Parallell execution. Could be fixed to a single Client java file.

* Log Query Server. Listens for queries request.
	- Highlights.
		* Running port are defined on the usage of this program

* Log Query Server (Worker Thread). Executes query/queries based on grep format.
	- Highlights
		* Usage of Runtime/Process for local execution of grep format
		* Allows regex.

* Server Properties. Defines working ip/port for server execution.

* Log Query Formatter. Defines the proposed format for log generation

* Log Generator. Creates random logs for such machine with fixed size .log file
