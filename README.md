# Log-query-system
Distributed log query processing system.

# General marks
* Missing packaging on .jar file.
* Request grep calls, so far on Windows could not work by the moment.
* Check Log Query Client and define hard-coded ip address. Run Server on the same port.

# Modules implemented
2018-12-24

* Log Query Client. Connect to the servers.
	- Highlights.
		* Servers hard-coded on function addServer(ServerProperty server)
		* Working subnet may vary according to connection. Could be fixed.
		* Workers result (Log from workers) fetched on try/catch. Could be fixed.


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
