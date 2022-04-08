pipeline {
    agent any
    stages {
        stage('whoami') {
            steps {
                sh 'echo $(whoami)'
            }
        }
        stage('ls -al') {
            steps {
                sh 'echo $(ls -al)'
            }
        }
        stage('Gradlew grant Execute Permission') {
            steps {
                sh 'chmod +x ./gradlew'
            }
        }
        stage('ls -al2') {
                    steps {
                        sh 'echo $(ls -al)'
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