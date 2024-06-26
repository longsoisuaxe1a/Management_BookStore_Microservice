pipeline {
    agent any
    tools {
        maven 'Maven 3.9.6'
    }
    stages {
        stage('Clone Repository') {
            steps {
                git credentialsId: 'hau', url: 'https://gitlab.com/longsoisuaxe1a/Management_BookStore_Microservice', branch: 'main'
            }
        }
        stage('Build and Push Docker Image') {
            steps {
                withDockerRegistry(credentialsId: 'docker-v2', url: 'https://index.docker.io/v1/') {
                    dir('BookService') {
                        sh 'mvn clean package -DskipTests'
                        sh 'docker build -t bourbon692/book-service:0.0.1 .'
                        sh 'docker push bourbon692/book-service:0.0.1'
                    }
                    dir('APIGateway') {
                        sh 'mvn clean package -DskipTests'
                        sh 'docker build -t bourbon692/api-gateway-service:0.0.1 .'
                        sh 'docker push bourbon692/api-gateway-service:0.0.1'
                    }
                    dir('CartService') {
                        sh 'mvn clean package -DskipTests'
                        sh 'docker build -t bourbon692/cart-service:0.0.1 .'
                        sh 'docker push bourbon692/cart-service:0.0.1'
                    }
                    dir('DiscoveryService') {
                        sh 'mvn clean package -DskipTests'
                        sh 'docker build -t bourbon692/discovery-service:0.0.1 .'
                        sh 'docker push bourbon692/discovery-service:0.0.1'
                    }
                    dir('OrderService') {
                        sh 'mvn clean package -DskipTests'
                        sh 'docker build -t bourbon692/order-service:0.0.1 .'
                        sh 'docker push bourbon692/order-service:0.0.1'
                    }
                    dir('UserService') {
                        sh 'mvn clean package -DskipTests'
                        sh 'docker build -t bourbon692/user-service:0.0.1 .'
                        sh 'docker push bourbon692/user-service:0.0.1'
                    }
                    dir('JWTService') {
                        sh 'mvn clean package -DskipTests'
                        sh 'docker build -t bourbon692/jwt-service:0.0.1 .'
                        sh 'docker push bourbon692/jwt-service:0.0.1'
                    }
                }
            }
        }
    }
}
