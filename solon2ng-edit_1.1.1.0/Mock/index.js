'use strict'
// https://www.npmjs.com/package/mockserver
var http = require('http');
var mockserver = require('mockserver');
var port = 9001;
var mocks = 'solon-edit';
var verbose = true;
console.log('Mockserver serving Solon-edit mocks {'
    + 'verbose'.yellow + ':' + (verbose && 'true'.green || 'false')
    + '} under "' + mocks.green + '" at '
    + 'http://localhost:'.green + port.toString().green);

mockserver.headers = ['authorization', 'X-token'];
var ms = mockserver(mocks, verbose);
http.createServer(ms).listen(port);