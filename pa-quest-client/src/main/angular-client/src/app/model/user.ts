
export class User {
    
    role: string[];

    constructor(public id: number, public name: string, public username: string, public email: string, public password: string, public passwordConfirm: string) {

    }

}