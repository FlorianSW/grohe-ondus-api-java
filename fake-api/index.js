const http = require('http');
const url = require('url');
const token = require('./responses/token.json');
const dashboard = require('./responses/dashboard.json');
const location = require('./responses/locations/14521.json');
const room = require('./responses/locations/rooms/23547.json');
const guard = require('./responses/locations/rooms/appliances/550e8400-e29b-11d4-a716-446655440000.json');
const guardAggregatedData = require('./responses/locations/rooms/appliances/data/aggregated/550e8400-e29b-11d4-a716-446655440000.json');
const guardNotifications = require('./responses/locations/rooms/appliances/notifications/550e8400-e29b-11d4-a716-446655440000.json');
const guardCommand = require('./responses/locations/rooms/appliances/command/550e8400-e29b-11d4-a716-446655440000.json');
const blue = require('./responses/locations/rooms/appliances/550e8400-e29b-11d4-a716-446655440001.json');
const blueCommand = require('./responses/locations/rooms/appliances/command/550e8400-e29b-11d4-a716-446655440001.json');
const port = 3000;

function ifAuthenticated(request, response, callback, writeHead = true) {
    const auth = request.headers['authorization'];
    if (!auth) {
        return false;
    }
    if (auth === "Bearer AN_ACCESS_TOKEN") {
        if (writeHead) response.writeHead(200, {"Content-Type": "application/json"});
        callback(response);
        response.end();
    } else {
        response.writeHead(403);
    }
}

let notifications = guardNotifications;
let actualGuardCommand = guardCommand;
let actualBlue = blue;
let actualBlueCommand = blueCommand;
const requestHandler = (request, response) => {
    console.log('[REQUEST] ' + request.method + ': ' + request.url);
    const parsed = url.parse(request.url);
    if (parsed.pathname === "/resetState") {
        notifications = guardNotifications;
        actualGuardCommand = guardCommand;
        actualBlue = blue;
        actualBlueCommand = blueCommand;
        response.writeHead(200);
        response.end();
    } else if (parsed.pathname === "/v3/iot/oidc/token") {
        response.writeHead(200, {"Content-Type": "application/json"});
        response.write(JSON.stringify(token));
        response.end();
    } else if (parsed.pathname === '/v3/iot/oidc/refresh') {
        response.writeHead(200, {"Content-Type": "application/json"});
        response.write(JSON.stringify(token));
        response.end();
    }  else if (parsed.pathname === '/v3/iot/dashboard') {
        ifAuthenticated(request, response, function (resp) {
            resp.write(JSON.stringify(dashboard));
        });
    } else if (parsed.pathname === '/v3/iot/locations') {
        ifAuthenticated(request, response, function (resp) {
            resp.write(JSON.stringify([location]));
        });
    } else if (parsed.pathname === '/v3/iot/locations/14521') {
        ifAuthenticated(request, response, function (resp) {
            resp.write(JSON.stringify(location));
        });
    } else if (parsed.pathname === '/v3/iot/locations/14521/rooms') {
        ifAuthenticated(request, response, function (resp) {
            resp.write(JSON.stringify([room]));
        });
    } else if (parsed.pathname === '/v3/iot/locations/14521/rooms/23547') {
        ifAuthenticated(request, response, function (resp) {
            resp.write(JSON.stringify(room));
        });
    } else if (parsed.pathname === '/v3/iot/locations/14521/rooms/23547/appliances') {
        ifAuthenticated(request, response, function (resp) {
            resp.write(JSON.stringify([guard, actualBlue]));
        });
    } else if (parsed.pathname === '/v3/iot/locations/14521/rooms/23547/appliances/550e8400-e29b-11d4-a716-446655440000' && request.method === 'GET') {
        ifAuthenticated(request, response, function (resp) {
            resp.write(JSON.stringify([guard]));
        });
    } else if (parsed.pathname === '/v3/iot/locations/14521/rooms/23547/appliances/550e8400-e29b-11d4-a716-446655440000/data/aggregated' && request.method === 'GET') {
        ifAuthenticated(request, response, function (resp) {
            resp.write(JSON.stringify(guardAggregatedData));
        });
    } else if (parsed.pathname === '/v3/iot/locations/14521/rooms/23547/appliances/550e8400-e29b-11d4-a716-446655440000/notifications' && request.method === 'GET') {
        ifAuthenticated(request, response, function (resp) {
            resp.write(JSON.stringify(notifications));
        });
    } else if (parsed.pathname === '/v3/iot/locations/14521/rooms/23547/appliances/550e8400-e29b-11d4-a716-446655440000/notifications/5f7168b6-b0ea-4a6b-9257-667a0bb62eb9' && request.method === 'PUT') {
        let data = '';
        request.on('data', chunk => {
            data += chunk.toString();
        });
        request.on('end', () => {
            ifAuthenticated(request, response, function (resp) {
                const parsedData = JSON.parse(data);
                if (parsedData.is_read === true) {
                    notifications = [];
                } else if (parsedData.is_read === false) {
                    notifications = guardNotifications;
                } else {
                    resp.writeHead(400);
                    resp.end();
                    return;
                }
                resp.writeHead(200, {"Content-Type": "application/json"});
                resp.end();
            }, false);
        });
    } else if (parsed.pathname === '/v3/iot/locations/14521/rooms/23547/appliances/550e8400-e29b-11d4-a716-446655440000/command' && request.method === 'GET') {
        ifAuthenticated(request, response, function (resp) {
            resp.write(JSON.stringify(actualGuardCommand));
        });
    }  else if (parsed.pathname === '/v3/iot/locations/14521/rooms/23547/appliances/550e8400-e29b-11d4-a716-446655440000/command' && request.method === 'POST') {
        let data = '';
        request.on('data', chunk => {
            data += chunk.toString();
        });
        request.on('end', () => {
            ifAuthenticated(request, response, function (resp) {
                actualGuardCommand = JSON.parse(data);
                resp.writeHead(201, {"Content-Type": "application/json"});
                resp.write(data);
            }, false);
        });
    } else if (parsed.pathname === '/v3/iot/locations/14521/rooms/23547/appliances/550e8400-e29b-11d4-a716-446655440001' && request.method === 'GET') {
        ifAuthenticated(request, response, function (resp) {
            resp.write(JSON.stringify([actualBlue]));
        });
    } else if (parsed.pathname === '/v3/iot/locations/14521/rooms/23547/appliances/550e8400-e29b-11d4-a716-446655440001/command' && request.method === 'GET') {
        ifAuthenticated(request, response, function (resp) {
            resp.write(JSON.stringify(actualBlueCommand));
        });
    }  else if (parsed.pathname === '/v3/iot/locations/14521/rooms/23547/appliances/550e8400-e29b-11d4-a716-446655440001/command' && request.method === 'POST') {
        let data = '';
        request.on('data', chunk => {
            data += chunk.toString();
        });
        request.on('end', () => {
            ifAuthenticated(request, response, function (resp) {
                actualBlueCommand = JSON.parse(data);
                resp.writeHead(201, {"Content-Type": "application/json"});
                resp.write(data);
            }, false);
        });
    }  else if (parsed.pathname === '/v3/iot/locations/14521/rooms/23547/appliances/550e8400-e29b-11d4-a716-446655440001' && request.method === 'PUT') {
        let data = '';
        request.on('data', chunk => {
            data += chunk.toString();
        });
        request.on('end', () => {
            ifAuthenticated(request, response, function (resp) {
                const parsedData = JSON.parse(data);
                if (parsedData.params.water_hardness !== undefined) {
                    resp.writeHead(400);
                    resp.end();
                    return;
                }
                actualBlue = parsedData;
                resp.writeHead(201, {"Content-Type": "application/json"});
                resp.write(data);
            }, false);
        });
    } else {
        response.writeHead(404);
        response.end();
    }
};

const server = http.createServer(requestHandler);

server.listen(port, (err) => {
    if (err) {
        return console.error('Could not start fake server', err)
    }
    console.log('Listening on ' + port);
});
