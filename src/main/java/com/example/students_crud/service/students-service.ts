// @ts-ignore
// @ts-ignore
// @ts-ignore
// @ts-ignore
// @ts-ignore
// @ts-ignore

@Injectable({
    provideIn:'root'
})
// @ts-ignore
class StudentsService{
    studentURL="http://localhost:8080/students"
    // @ts-ignore
    constructor(private httpClient:HttpClient) {}


    generateStudentListPdf(){
        return this.httpClient.get(this.studentURL+'/pdf')

    }
}