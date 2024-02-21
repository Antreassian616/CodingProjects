//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Factor C++ Program
// This program spawns children, who will help to determine the prime numbers.
// To compile:  g++ Factor.cpp -o Factor
// To run:  ./runFactor > temp.out
// To monitor system resources: (Linux): Applications->System Tools->System Monitor->Resources
// Use Spreadsheet Applications->Office->LibreOffice Calc to show efficiency of multiple threads.
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
#include <iostream>
#include <stddef.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <ctime>
#include <stdlib.h>
using namespace std;

#define numChild  1

#define total  600000

class Factor 
{
public:
    Factor() { }
    void child(int begin, int end);
private:
    int start_s;     // start time
};

int main()
{
	int i;
	int range = total/numChild;
	int begin=0;
	int stat;

	cerr << "Run Factor " << total << ":" << numChild << endl;
	Factor factor;

	// Spawn children processes
	for (i=0; i<numChild; i++) {
	    if (fork() == 0) factor.child(begin, begin+range);
	    begin += range + 1;
	}

	// Wait for children to finish
	for (i=0; i<numChild; i++)
	   wait(&stat);
	cerr << "All Children Done: " << numChild << endl;
}

void Factor::child(int begin, int end)
{
    // Get the current time
    start_s=clock();
    int val, i;
    for (val=begin; val<end; val++) {
	for (i=2; i<=val/2; i++) 
            if (val % i == 0) break;
	/* For faster run, comment out the two instructions below */
        if (i>val/2)
	    cout << "F:" << val << endl;
    }

    // Calculate execution time in ms and print
    int stop_s=clock();
    cerr << "time: " << (stop_s-start_s)/double(CLOCKS_PER_SEC)*1000 << endl;
    exit(0);
}
