# CS6650 Assignment1

## Repo URL
[Link to GitHub](https://github.com/zhan-xl/CS6650-assignment1.git)

## Client Design
### Overall structures and packages
The Main class serves as the entry point where it utilizes three services that I have created. In the main method, a PostRequestService handles making the 200k post request to the API. The class also has an inner Builder class enabling easy modification of parameters; a Writer class that uses a list of responses to write the log file including start time, request methods, latency, and response code; and a Printer class to print the time result on the terminal.

There are three major packages inside the client:

* An API package includes the HTTP client that handles all the API calls to the server. For this assignment, only the post API in the HTTP client is used.
* A Models package includes the classes that only contain information. Inside the package, the RideEntry class represents all the information that a post request needs, such as the path parameters and body. The API client takes a single object and breaks it up to create the path and body for the post request. Another class in the package is the ResponseLog class that represents the information of a single response to write it into a CSV file later by the Writer Service.
* A Services package includes the service classes for the main function to use.

### Post Request Service class
This is the core class in the application, which sends 200k post requests to the server implemented by the method called makePostRequest(). In this method, I created an Executor Service for the producer thread to generate random ride entries. These ride entries are then stored inside a BlockingQueue for the consumer threads to use. By doing it this way, it would not take too much memory to save all 200k ride entry information before making the post request. The challenge is to make enough producer threads to keep up with the consumer thread usage but not too many threads that slow down the whole process.

The consumer threads take a ride entry from the blocking queue and send the request. Initially, according to the instruction, I created 32 threads and stored them inside a list. I used a while loop to check if any of the threads are done. If so, the main method would start more threads until it reaches a maximum thread number that I set ahead. Then it will use those threads to finish the rest of the post requests. I kept changing the number of maximum threads to find out the fastest way to complete the requests. Since the total number of requests is 200k and each thread should make 1000 requests, the total number of threads we need is 200 threads. Since we started with 32, then there are 168 threads left. So, I found that to run the system as fast as possible, we need no fewer than 168 threads to run after the initial 32 threads. This way, all the remaining threads would end almost simultaneously.

