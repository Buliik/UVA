#include <iostream>

using namespace std;

int main()
{
    int num_of_block;
    int num_1;
    int num_2;
    int num_of_rows;
    bool overflow = false;
    char* result;

    cin >> num_of_block;

    for (int i = 0; i < num_of_block; i++) {
        cin >> num_of_rows;
        result = new char[num_of_rows + 1];
        result[num_of_rows] = '\0';
        for (int j = 0; j < num_of_rows; j++ ) {
            cin >> num_1;
            cin >> num_2;
            num_1 += num_2;
            overflow = num_1 >= 10;
            num_1 = (overflow ? num_1 - 10 : num_1);
            result[j] = num_1 + '0';
            for (int k = 1; overflow; k++) {
                num_1 = result[j - k] + 1;
                overflow = num_1 > '9';
                num_1 = (overflow ? num_1 - 10 : num_1);
                result[j - k] = num_1;
            }
        }
        cout << result << endl;
        if (i < num_of_block - 1){
            cout << endl;
        }
        delete result;
    }

    return 0;
}
