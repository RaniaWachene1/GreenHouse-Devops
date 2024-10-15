pipeline {
    agent any
    tools {
        jdk 'jdk17'
        maven 'maven3'
    }
    environment {
        SCANNER_HOME = tool 'sonar-scanner'
    }
    stages {
        stage('Git Checkout') {
            steps {
                git branch: 'main', credentialsId: 'git-cred', url: 'https://github.com/RaniaWachene1/GreenHouse-Devops.git'
            }
        }
        stage('Update Version') {
            steps {
                script {
                    def version = "1.0.0.${env.BUILD_NUMBER}"
                    sh "mvn versions:set -DnewVersion=${version} -DgenerateBackupPoms=false"
                }
            }
        }
        stage('Compile') {
            steps {
                sh "mvn compile"
            }
        }
        stage('Tests - JUnit/Mockito') {
            steps {
                sh 'mvn test'
            }
        }
        stage('JaCoCo Code Coverage') {
            steps {
                sh 'mvn jacoco:prepare-agent test jacoco:report'
            }
        }
        stage('File System Scan') {
            steps {
                sh "trivy fs --format table -o trivy-fs-report.html ."
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonar') {
                    sh '''
                    $SCANNER_HOME/bin/sonar-scanner \
                    -Dsonar.projectName=GreenHouse \
                    -Dsonar.projectKey=GreenHouse \
                    -Dsonar.java.binaries=.
                    '''
                }
            }
        }
        stage('Quality Gate') {
            steps {
                script {
                    waitForQualityGate abortPipeline: false, credentialsId: 'sonar-token'
                }
            }
        }
        stage('Build') {
            steps {
                sh "mvn package"
            }
        }
            stage('OWASP Dependency Check') {
                    steps {
                        script {
                            withCredentials([string(credentialsId: 'nvd-api-key', variable: 'NVD_API_KEY')]) {
                                dependencyCheck additionalArguments: '''
                                    --scan ./ \
                                    --format ALL \
                                    --out . \
                                    --prettyPrint \
                                    --nvdApiKey ${NVD_API_KEY}
                                ''', odcInstallation: 'DC'

                                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
                            }
                        }
                    }
                }
        stage('Publish To Nexus') {
            steps {
                withMaven(globalMavenSettingsConfig: 'global-settings', jdk: 'jdk17', maven: 'maven3', mavenSettingsConfig: '', traceability: true) {
                    sh "mvn deploy"
                }
            }
        }
        stage('Build & Tag Docker Image') {
            steps {
                script {
                    withDockerRegistry(credentialsId: 'docker-cred', toolName: 'docker') {
                        sh "docker build -t raniawachene/greenhouse:latest ."
                    }
                }
            }
        }
        stage('Docker Image Scan') {
            steps {
                sh "trivy image --format table -o trivy-image-report.html raniawachene/greenhouse:latest"
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    withDockerRegistry(credentialsId: 'docker-cred', toolName: 'docker') {
                        sh "docker push raniawachene/greenhouse:latest"
                    }
                }
            }


        }
      stage('Docker Compose Backend & MySQL') {
            steps {
                script {
                    // Ensure MySQL is not already running on the host machine
                    sh 'docker-compose down || true' // Stops any previous containers
                    sh 'docker-compose up -d' // Starts your MySQL and backend services
                }
            }
        }
    }

    post {
        always {
            script {
                def jobName = env.JOB_NAME
                def buildNumber = env.BUILD_NUMBER
                def pipelineStatus = currentBuild.result ?: 'UNKNOWN'
                def bannerColor = pipelineStatus.toUpperCase() == 'SUCCESS' ? 'green' : 'red'

                def body = """
                    <html>
                    <body>
                    <div style="border: 4px solid ${bannerColor}; padding: 10px;">
                    <h2>${jobName} - Build ${buildNumber}</h2>
                    <div style="background-color: ${bannerColor}; padding: 10px;">
                    <h3 style="color: white;">Pipeline Status: ${pipelineStatus.toUpperCase()}</h3>
                    </div>
                    <p>Check the <a href="${BUILD_URL}">console output</a>.</p>
                    </div>
                    </body>
                    </html>
                """

                emailext (
                    subject: "${jobName} - Build ${buildNumber} - ${pipelineStatus.toUpperCase()}",
                    body: body,
                    to: 'rania.wachene@esprit.tn',
                    from: 'raniawachen21@gmail.com',
                    replyTo: 'rania.wachene@esprit.tn',
                    mimeType: 'text/html',
                    attachmentsPattern: '**/dependency-check-report.xml,**/trivy-fs-report.html,**/trivy-image-report.html'
                )
            }
            archiveArtifacts artifacts: '**/dependency-check-report.xml', allowEmptyArchive: true
            archiveArtifacts artifacts: '**/trivy-fs-report.html', allowEmptyArchive: true
            archiveArtifacts artifacts: '**/trivy-image-report.html', allowEmptyArchive: true
        }
    }
}

