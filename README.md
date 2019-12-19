PidServices
===========
Code to interact with the EPIC Handle API.

Originally developed by Thomas Eckart @ University of Leipzig.

Configuration
-------------
To build project (including tests):
* copy config.properties_template to config.properties in src/test/resources
* set GWDG user/password in config.properties

Tests
-----
Test 'PidWriterTest': would create new Handle, right now omitted to reduce creation of useless test Handles (can be enabled in PidWriterTest.testPIDWriting() for complete tests)
