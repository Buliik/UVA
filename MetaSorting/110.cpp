#include <iostream>
#include <string>

char alphabet[8] = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };

using namespace std;

void sort(char vars[8], int n, int c);
string mezernik(int a);
string generator(char vars[8], int n, bool order);

int main()
{
	int n, m;
	char vars[8] = { 'a' };

	cin >> n;

	for (int i = 0; i < n; i++)
	{
		if (i != 0)
			cout << endl;

		cin >> m;

		cout << "program sort(input,output);\nvar\n";

		for (int i = 0; i < m; i++)
		{
			cout << alphabet[i];
			if (i != m - 1) cout << ",";
		}

		cout << " : integer;\nbegin\n  readln" << generator(alphabet, m, 0) << ";" << endl;

		sort(vars, m, 1);

		cout << "end." << endl;
	}
}

void sort(char vars[8], int n, int c)
{
	string mezery = mezernik(c);
	int pom;

	cout << mezery;

	if (c == n)
	{
		cout << "writeln" << generator(vars, n, 1) << endl;
		return;
	}

	char nove[8];

	for (int i = 0; i <= c; i++)
	{
		pom = 0;

		if (i > 0) cout << mezery << "else";
		if (i > 0 && i < c) cout << " ";
		if (i < c) cout << "if " << vars[i] << " < " << alphabet[c] << " then";


		for (int j = 0; j <= c; j++)
		{
			if (j == i)
			{
				pom = 1;

				nove[j] = alphabet[c];
			}
			nove[j + pom] = vars[j];
		}

		cout << endl;

		sort(nove, n, c + 1);
	}



}

string generator(char vars[8], int n, bool order)
{
	string vystup = "(";

	if (order)
		for (int i = n - 1; i >= 0; i--)
		{
			vystup += vars[i];
			if (i != 0) vystup += ",";
		}

	else
		for (int i = 0; i < n; i++)
		{
			vystup += vars[i];
			if (i != n - 1) vystup += ",";
		}
	vystup += ")";
	return vystup;
}

string mezernik(int a)
{
	string mezery = "";
	for (int i = 0; i < a; i++)
	{
		mezery += "  ";
	}

	return mezery;
}