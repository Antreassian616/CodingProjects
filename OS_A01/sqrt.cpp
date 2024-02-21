/////////////////////////////////////////////////////////////////////////////////////////////////////////////
// SquareRoot C++ Program
// This program spawns children, who will help to determine square roots.
// To compile:  g++ sqrt.cpp -o sqrt
// To run:  ./sqrt > sqrt.out
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
//#include <processthreadsapi.h>   // Windows: GetCurrentProcessorNumber()
#include <utmpx.h>
#include <math.h>
using namespace std;

#define numChild  6
#define total  100     // 200000000
double global=0.0;

class SquareRoot
{
public:
    SquareRoot() { memAttr=0.0; }
    void child(double begin, double end);
private:
    int start_s;     // start time
    double memAttr;
};

int main()
{
    int i;
    double range = total/numChild;
    double begin=5999901.0;
    int stat;

    cerr << "Run SquareRoot " << total << ":" << numChild << endl;
    SquareRoot Sqrt;

    // Spawn children processes
    for (i=0; i<numChild; i++) {
        if (fork() == 0) Sqrt.child(begin, begin+range);
        begin += range + 1;
    }

    // Wait for children to finish
    for (i=0; i<numChild; i++)
        wait(&stat);
    cerr << "All Children Done: " << numChild << endl;
}

void SquareRoot::child(double begin, double end) {
    // Print the current CPU number
    //cerr << "CPU:"<< GetCurrentProcessorNumber() << endl; // Windows 
    //cerr << "CPU=" << sched_getcpu() << endl;               // Linux version

    // Get the current time
    start_s = clock();
    double totalSum = 0.0;
    for (int local = begin; local < end; local++) {
        double root = sqrt(local);
        // revised lines to do prints:
        cout << local << ":" << root << " ";
        if (local%5==0) cout << endl;
        totalSum += root;
    }

    // Are class attributes and globals shared?
    cerr << "  totalSum=" << totalSum << " global=" << ++global << " memAttr=" << ++memAttr << endl;
    // Calculate execution time in ms and print
    int stop_s = clock();
    cerr << "time: " << (stop_s - start_s) / double(CLOCKS_PER_SEC) * 1000 << endl;
    exit(0);
}
