import logging
from logging import StreamHandler, Formatter
from logging.handlers import SocketHandler
from flask import Flask, request, g
from datetime import datetime

app = Flask(__name__)


# Custom handler to ensure UTF-8 encoding
class UTF8SocketHandler(SocketHandler):
    def makePickle(self, record):
        # Format the log record as JSON and encode it as UTF-8
        log_entry = self.format(record)
        return (log_entry + "\n").encode('utf-8')


# Custom formatter to include ISO 8601 timestamp
class ISO8601Formatter(Formatter):
    def formatTime(self, record, datefmt=None):
        return datetime.fromtimestamp(record.created).isoformat()

    def format(self, record):
        record.traceId = getattr(g, 'traceId', 'no-trace-id')
        record.spanId = getattr(g, 'spanId', 'no-span-id')
        return super().format(record)


# Create a logger
logger = logging.getLogger('python-logstash-logger')
logger.setLevel(logging.INFO)

# Console handler for local debugging
# console_handler = StreamHandler()
# console_formatter = ISO8601Formatter(
#     '{"timestamp":"%(asctime)s","level":"%(levelname)s","message":"%(message)s", "traceId": "%(traceId)s", "spanId": "%(spanId)s"}'
# )
# console_handler.setFormatter(console_formatter)
# logger.addHandler(console_handler)

# Logstash handler with UTF-8 encoding and ISO 8601 timestamp
logstash_handler = UTF8SocketHandler('elk', 4560)
logstash_formatter = ISO8601Formatter(
    '{"@timestamp":"%(asctime)s","level":"%(levelname)s","message":"%(message)s", "traceId": "%(traceId)s", "spanId": "%(spanId)s"}'
)
logstash_handler.setFormatter(logstash_formatter)
logger.addHandler(logstash_handler)


@app.before_request
def before_request():
    traceparent = request.headers.get('Traceparent')
    if traceparent:
        parts = traceparent.split('-')
        if len(parts) == 4:
            g.traceId = parts[1]
            g.spanId = parts[2]
        else:
            g.traceId = 'invalid-traceparent'
            g.spanId = 'invalid-traceparent'
    else:
        g.traceId = 'no-trace-id'
        g.spanId = 'no-span-id'

    # Log all request headers
    logger.info('Request headers: %s', dict(request.headers))


@app.route('/api/ms4', methods=['GET'])
def endpoint():
    # Log the request with trace and span IDs
    logger.info('Received request for /api/ms4')

    # Additional logs for verification
    logger.debug('Debug message from ms-4')
    logger.info('Info message from ms-4')
    logger.warning('Warning message from ms-4')
    logger.error('Error message from ms-4')
    logger.critical('Critical message from ms-4')

    return {"message": "Response from ms-4"}


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8084)
