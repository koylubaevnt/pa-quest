export class SignUpInfo {
    
    role: string[];

    constructor(private name: string, private username: string, private email: string, private password: string, private passwordConfirm: string) {
        this.role = ['user']
    }

}