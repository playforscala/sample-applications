# 3.3.5 Using Slick for database access

Sample application from chapter 3 of [Play for Scala](http://bit.ly/playscala).

This sample is a Scala script (not a Play application) that uses Slick to perform a simple database query and prints the results.


## Running the example

1. Install pre-requisites. The following set-up is for PostgreSQL and sbt on OS X.

2. Create and run a local PostgreSQL database called `slick`, and a user `slick` with password `slick`:

		$ sudo su - postgres
		$ createuser -S -D -R -P slick
		Enter password for new role: slick
		Enter it again: slick
		CREATE ROLE
		$ createdb -E UNICODE -O slick slick "Slick example"
		CREATE DATABASE
		COMMENT

3. Start the database:

		pg_ctl -D /usr/local/var/postgres -l /usr/local/var/postgres/server.log start

4. Run the example:

		$ sbt
		> run

The output should show the following database query results:

	(5018206244666,Giant Paperclips,Giant Plain 51mm 100 pack)
	(5018306312913,No Tear Paper Clip,No Tear Extra Large Pack of 1000)
	(5018306332812,Paperclip Giant Plain,Giant Plain Pack of 10000)
	(5010255079763,Paperclips Large,Large Plain Pack of 1000)
	(5018206244611,Zebra Paperclips,Zebra Length 28mm Assorted 150 Pack)
