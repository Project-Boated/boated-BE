pipeline {
    agent any
    stages {
        stage('Gradlew grant Execute Permission') {
            steps {
                sh 'chmod +x ./gradlew'
            }
        }
        stage('Clean') {
            steps {
                sh './gradlew clean'
            }
        }
        stage('Test') {
            steps {
                sh './gradlew test'
            }
        }
        stage('Build') {
            steps {
                sh './gradlew bootJar -x test'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploy'
            }
        }
    }
}