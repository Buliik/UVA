#include <iostream>

using namespace std;

int main()
{
	unsigned int input = 0;

	while (cin >> input)
	{
	    cout << ((int)input) << " converts to ";
		input = (input << 24) | (input >> 24) | ((input << 8) & 0xFF0000) | ((input >> 8) & 0xFF00);
		cout << ((int)input) << endl;
	}

	return 0;
}
