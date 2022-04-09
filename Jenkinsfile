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
                sh './gradlew test -Dorg.gradle.java.home=/usr/lib/jvm/java-17-openjdk-amd64'
            }
        }
        stage('Build') {
            steps {
                sh './gradlew bootJar -x test -Dorg.gradle.java.home=/usr/lib/jvm/java-17-openjdk-amd64'
            }
        }
        stage('Make Docker Image') {
            steps {
                sh 'docker build -t boated-be .'
            }
        }
        stage('Save Docker Image to Tar') {
            steps {
                sh 'docker save -o boated-be.tar boated-be'
            }
        }
        stage('ssh') {
            steps {
                sh 'scp boated-be.tar ubuntu@15.164.89.188:/home/ubuntu/boated'
                sh 'ssh ubuntu@15.164.89.188 "cd boated ; sh boated.sh"'
            }
        }
        stage('Delete Docker Image') {
            steps {
                sh 'docker rmi boated-be'
                sh 'rm boated-be.tar'
            }
        }
    }
}