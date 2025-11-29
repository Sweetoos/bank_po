import java.util.HashMap;
import java.util.Map;

public class Bank {
    private final Map<Integer, Client>  clients;
    private final Map<Integer, Integer> accountList; //First int is accNumber, second int is client ID.

    private final MainVariables mv;

    private final IOConsole     IO;
    private final UserInterface UI;

    public  final String appVersion;

    private final Checks ch;

    Bank(){
        this.appVersion = "2.1.20";

        clients = new HashMap<>();
        accountList = new HashMap<>();

        mv = new MainVariables();

        IO = IOConsole.start(this.appVersion);
        UI = new UserInterface(appVersion, IO);

        ch = new Checks();

        doTasks();
    }


    // -- working functions --

    public void doTasks(){
        ch.opOne = -1;

        while(true){
            ch.opOne = UI.firstLayer();

            if(ch.opOne == 1){
                addClient();

            } else if(ch.opOne == 2){

                optionTwo();

            } else if(ch.opOne > 2 || ch.opOne < 0){
                break;
            }

            if(IO.isEndPr()){
                break;
            }
            ch.opOne = -1;
        }
    }

    private void optionTwo(){ // Making operations on a user.
        ch.opTwo = -1;
        ch.opThree = -1;
        //ch.opFour = -1;

        Client client;

        ch.opTwo = UI.chooseClient(this.clients);
        client = this.clients.get(ch.opTwo);

        ch.opThree = UI.chooseOptionTwo(client);

        int accId = client.getMainAccountId();
        switch(ch.opThree){
            case 1:
                accId = help1(accId, client);

                client.getAcc(accId).deposit();
                break;
            case 2:
                accId = help1(accId, client);

                client.getAcc(accId).withdraw();
                break;
            case 3:
                accId = help1(accId, client);

                executeTransaction( client.getAcc(accId).transfer() );
                break;
            case 4:


                break;
            case 5:
                accId = client.addAccount();//komplenie do wymiany

                UI.addAcc(client.getAcc(accId));
                break;
            case 6:
                //Do uzupełnienia
                break;
            case 7:
                //Do uzupełnienia
                break;
            default:
                //Error
                break;
        }


    }


    // --
    private int help1(int accId, Client client){ //Helping function for optionTwo.

        if(client.isUseMaOp()){ // isUseMainAccountOption
            return accId;
        }

        if(UI.fromMainAcc() != 1){
            return UI.showAccList( client.createAccList() );
        }

        return accId;
    }
    // --

    // -- --






    public void addClient(){
        ClientData cd = new ClientData();
        cd.clientId = mv.newClientId();

        cd = UI.addClient(cd);

        mv.incLastAccountNumber();

        Client client = new Client(
                cd,
                mv,
                UI,
                this
        );
        this.clients.put(mv.getLastClientId(), client);
    }

    /* Function registers account number that was just created.
     * Function gets clientId that represents to witch client this account is belonging, and
     * puts lastAccountNumber from mv object (that carries main variables needed in project)
     * with clientId.
     */
    public void registerAcc(int clientId){
        this.accountList.put(mv.getLastAccountNumber(), clientId);
    }


    // -- --

    private void executeTransaction(TransactionData tx){

        if(!tx.makeTransaction){
            return; //Wyrzucenie wyjątku braku przelewu.
        }

        //Transaction is returned to the original account when destination account is not found.
        if(!accountList.containsKey(tx.inAccNumber)){

            tx.inAccNumber = tx.outAccNumber;
        }

        int clientId = accountList.get(tx.inAccNumber);
        Client client = this.clients.get(clientId);

        client.executeTransaction(tx);
    }

    // -- --



    // -- Internal Class --

    public class Checks{

        public int opOne;
        public int opTwo;
        public int opThree;
        public int opFour;

        Checks(){
            opOne   = 0;
            opTwo   = 0;
            opThree = 0;
            opFour  = 0;
        }

    }

}
