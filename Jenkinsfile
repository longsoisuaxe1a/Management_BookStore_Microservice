pipeline {
    agent any
    tools {
        docker 'docker' // Sử dụng tên công cụ Docker đã được cấu hình trong Jenkins
    }
    stages {
        stage('Clone Repository') {
            steps {
                git credentialsId: 'microservice-network-1', url: 'https://gitlab.com/longsoisuaxe1a/Management_BookStore_Microservice', branch: 'main'
            }
        }
    }
}
