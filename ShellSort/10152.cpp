#include <iostream>
#include <string>

using namespace std;

class Stack {
public:
	void push(string val);
	string pop();
	string peek();
	bool isEmpty();
private:
	string turtles[200];
	int top = -1;

};

int main()
{
	int n, a;
	Stack zelvy, zelvicky;
	string data;
	cin >> n;
	for (int i = 0; i < n; i++)
	{
		cin >> a;
		cin.get();

		for (int j = 0; j < a; j++)
		{
			getline(cin, data);
			zelvy.push(data);
		}

		for (int j = 0; j < a; j++)
		{
			getline(cin, data);
			zelvicky.push(data);
		}

		while (!zelvy.isEmpty())
		{
			if (zelvy.pop() == zelvicky.peek())
			{
				zelvicky.pop();
			}
		}

		while (!zelvicky.isEmpty())
		{
			cout << zelvicky.pop() << endl;
		}

		cout << endl;

	}

	return 0;
}

void Stack::push(string val)
{
	turtles[++top] = val;
}

string Stack::pop()
{
	return turtles[top--];
}

string Stack::peek()
{
	return turtles[top];
}

bool Stack::isEmpty() {
	return top == -1;
}
