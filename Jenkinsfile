pipeline {
    agent any
    stages {
        stage('Send Slack Start') {
            agent any
            steps {
                slackSend (channel: '#jenkins', color: '#FFFF00', message: "STARTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
            }
        }
        stage('Copy Credential') {
            steps {
                sh 'cp /var/jenkins_home/boated-be/application-deploy.properties src/main/resources/application-deploy.properties'
                sh 'cp /var/jenkins_home/boated-be/application-develop.properties src/main/resources/application-develop.properties'
            }
        }
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
        stage('Build Docker Image') {
            steps {
                sh 'docker build -t public.ecr.aws/g7j4u9e2/boated-be-develop:$(git rev-parse --short HEAD) -t public.ecr.aws/g7j4u9e2/boated-be-develop:latest -f Dockerfile-develop .'
                sh 'docker build -t public.ecr.aws/g7j4u9e2/boated-be-deploy:$(git rev-parse --short HEAD) -t public.ecr.aws/g7j4u9e2/boated-be-deploy:latest -f Dockerfile-deploy .'
            }
        }
        stage('Send Docker Image to Repository') {
            steps {
                sh 'aws ecr-public get-login-password --region us-east-1 | docker login --username AWS --password-stdin public.ecr.aws'
                sh 'docker push public.ecr.aws/g7j4u9e2/boated-be-develop:$(git rev-parse --short HEAD)'
                sh 'docker push public.ecr.aws/g7j4u9e2/boated-be-develop:latest'
                sh 'docker push public.ecr.aws/g7j4u9e2/boated-be-deploy:$(git rev-parse --short HEAD)'
                sh 'docker push public.ecr.aws/g7j4u9e2/boated-be-deploy:latest'
            }
        }
        stage('Develop Server work') {
            steps {
                sh 'ssh ubuntu@129.154.200.226 "cd boated ; sh boated-be.sh"'
            }
        }
        stage('Delete Dangling Image') {
            steps {
                sh 'docker image prune -f'
            }
        }
    }
    post {
        success {
            slackSend (channel: '#jenkins', color: '#00FF00', message: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
        failure {
            slackSend (channel: '#jenkins', color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
    }
}