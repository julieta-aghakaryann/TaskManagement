# Task Management System



## Task information

Ունենք երկու տիպի Role (Manager , Employee). User- ները Login են լինում համակարգ և ամեն մեկը ըստ
իր Role-ի հնարավորության կատող է անել հետևյալ գործողույունները:
Manager
Երբ Manager-ը մտնում է համակարգ , նա տեսնում է բոլոր user – ների Task-երը: Ցուցակում թող երևան
Task Name, Task creation Date, Task update Date, Task status (New Task, Bug, In Process,
ReOpen, Resolved, Done) և user-ը որին տրված է Task-ը: Task-ը նաև ունի Description դաշտ , որը
նկարագորում է Task-ը:
Manager-ը կարող է ստեղծել Task և այն տալ user –ին: Task –ը ստեղծելիս task status - New Task է:
Manager-ը կարող է փոփոխել արդեն ստեղցված Task-ը և այն տալ մեկ այլ user –ի: Done և ReOpen
status-ը կարող է դնել միայն Manager-ը:
Task –եիր ցուցակի վերին մասում դնել Filter , որի միջոցով հնարավոր է ցուցակը ֆիլտրել , դնել filter – user
, status. Այս ֆիլտերների միջոցով օրինակ ՝ փնտրում ենք Aram user – ի task-երը, կամ ըստ status –ի
փնտրում ենք ‘Resolved’ Task-երը:
For example

Manager-ը կարող է նաև ջնջել(delete) task-երը:
Employee
Employee Role ունեցող user-ը , մուտք գործելով համակարգ կարող է տեսնել միայն իր Task-երը: Կարող է
task-ի status-ը փոխել (օրինակ դնել Resolved): Կարող է նոր Task ստեղծել և այն միայն իրեն է կցվում: Չի
կարող Task-ը ջնջել(Delete անել): Employee համար Filter պանելը կարեղ է լինել ըստ Task status-ի և
ըստ Task creation Date –ի: Այսպես ՝

Ընդհանուր՝ Պարտադիր ունենալ Login page, password-ը պահել Md5 –ով կամ ավելի secure տարբերակով
DB –ում: Ունենալ բոլոր Էջում կամ էջերում Logout. Եթե Logout եղել ես էլ չես կարեղ իմանալով page – ի
URL մուտք գործել համակարգ: Նորից պետք է Login լինել:
Նախագծել DB - ն և գրել խնդիրը ձեր իմացած framework –ով:
### Used tools
```
# Gradle version 7.4
# Java version 17 
```

## Collaborator

[Julieta Aghakaryan](https://github.com/JulietaAghakaryan)
