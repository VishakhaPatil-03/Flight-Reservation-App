pipeline{
    agent any 
    stages {
        stage('Code-Pull'){
            steps{
                git branch: 'main', url: 'https://github.com/VishakhaPatil-03/Flight-Reservation-App.git'
            }
        }
        stage('Code-Build'){
            steps{
                sh '''
                    cd FlightReservationApplication
                    mvn clean package 
                '''
            }
        }
        stage('Docker-Build'){
            steps{
                sh '''
                    cd FlightReservationApplication
                    docker build -t vishupatil14/flight-reservation-pls-18:latest .
                '''
            }
        }
        stage('QA-Test'){
            steps{
                withSonarQubeEnv(installationName: 'sonar', credentialsId: 'sonar-token') {
                 sh '''
                    cd FlightReservationApplication
                    mvn sonar:sonar -Dsonar.projectKey=flight-reservation
                 '''
                }
            }
        }
        stage('docker-build'){
            steps{
                sh '''
                    cd FlightReservationApplication
                    docker build -t vishupatil14/flight-reservation-pls-18:latest .
                    docker push vishupatil14/flight-reservation-pls-18:latest
                    docker rmi vishupatil14/flight-reservation-pls-18:latest
                '''
            }
        }
        stage('Deploy-to-Kubernetes'){
            steps{
                sh '''
                    kubectl apply -f k8s/
                  
                '''
            }
        }

       
        
    
}
}