# files-analyzer
Small File Analyzer Application

## Ideas:
The service will receive a file id via an API call and will have to analyze that file. During the analysis process specific file info will be extracted and persisted to datastore.
The service will contain an API in order to view specific information about the persisted files.

The project is split between three components:
- Text File Injector
- Worker
- Query API

### Ideas:
- Both APIs (injection and query) should be protected. JWT token auth is required and must be pass to the header.
- The text file injector api component will generate a working task for every file received. The working item will be push to a queue. 
- The worker component will listen to the queue and will process the working items. Once a working item is received, the worker will do file validation and if everything is fine will write the file content into the datastore.
- Query API will expose APIs for querying files information saved by the worker component to the datastore.
