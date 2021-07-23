#include <winsock2.h>
#include <iostream>
#include <thread>
using namespace std;
int isAlive = 2333;
WSADATA wsaData;
SOCKET sListen;
SOCKET sClient;
SOCKADDR_IN local;
SOCKADDR_IN client;
#include "SyntaxAnalyze.h"
#pragma comment(lib,"ws2_32.lib")
#define mk make_pair
#define pb push_back
#define ms(x,y) memset(x,y,sizeof(x))

typedef long long ll;


int used = 1;
char consoleStr[2048];
int main() {


    char szMessage[1024];
    int ret;
    int iaddrSize = sizeof(SOCKADDR_IN);
    WSAStartup(0x0202, &wsaData);

    sListen = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

    local.sin_family = AF_INET;
    local.sin_port = htons(25565);
    local.sin_addr.s_addr = htonl(INADDR_ANY);
    bind(sListen, (struct sockaddr*)&local, sizeof(SOCKADDR_IN));

    listen(sListen, 1);

    cout << "HelloPhone Server started." << endl;
    thread input([&]()->void {
        while (1) {
            cout << ">";
            cin.getline(consoleStr, 2048);
            used = 0;
        }
    });
    input.detach();
    while (TRUE) {
        cout << "Prepared for accepting" << endl;
        sClient = accept(sListen, (struct sockaddr*)&client, &iaddrSize);
        char byte[2];
        byte[0] = 104;
        byte[1] = 105;
        send(sClient, byte, 2, 0);
        recv(sClient, szMessage, 2, 0);
        if (szMessage[0] != 104 || szMessage[1] != 105) {
            closesocket(sClient);
            continue;
        }
        printf("Accepted client:%s:%d\n", inet_ntoa(client.sin_addr),
            ntohs(client.sin_port));
        int iMode = 1;

        ioctlsocket(sClient, FIONBIO, (u_long FAR*) & iMode);
        int status = 1;
        isAlive = 2000;
        used = 1;
        thread t([&]()->void {
            status = 1;
            int cnt = 0;
            while (status) {
                if (!used) {
                    if (!InAnalyze(consoleStr)) {
                        cout << "Fine" << endl;
                        status = 0;
                        break;
                    }
                    int code;
                    if ((code = send(sClient, consoleStr, strlen(consoleStr), 0)) < 0) {
                        cout << "Lost connection: " << code << endl;
                        status = 0;
                        break;
                    }
                    used = 1;
                }
                cnt++;
                if (cnt & (1 << 10)) {
                    cnt = 0;
                    char poo[4] = "poo";
                    send(sClient, poo, strlen(poo), 0);
                }
                if (!isAlive--) {
                    status = 0;
                    break;
                }
                _sleep(10);
            }
        });


        while (status) {
            char m[1024];
            ms(m, 0);
            int c = recv(sClient, m, 1024, 0);
            if (c > 0) {
                int res = OutAnalyze(m);
                if (res == -1) {
                    status = 0;
                    break;
                }
            }
            else if (c == 0) {
                status = 0;
                break;
            }

            _sleep(100);
        }
        cout << "Disconnected" << endl;
        //TerminateThread(t.native_handle(), 0);
        if (0) {
            keybd_event(83, 0, 0, 0);
            keybd_event(84, 0, 0, 0);
            keybd_event(79, 0, 0, 0);
            keybd_event(80, 0, 0, 0);
            _sleep(111);
            keybd_event(13, 0, 0, 0);
        }
        t.join();
        closesocket(sClient);
    }
    return 0;

}