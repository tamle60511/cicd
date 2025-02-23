FROM jenkins/jenkins:lts
USER root
RUN pwd && whoami
RUN whereis jenkins
RUN ls /usr/share/jenkins

RUN apt-get update && apt-get install -y ca-certificates curl gnupg lsb-release \
        software-properties-common
RUN echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers
RUN curl -fsSLo /usr/share/keyrings/docker-archive-keyring.asc \
  https://download.docker.com/linux/debian/gpg
RUN echo \
  "deb [arch=$(dpkg --print-architecture) \
  signed-by=/usr/share/keyrings/docker-archive-keyring.asc] \
  https://download.docker.com/linux/debian \
  $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list
RUN apt-get update && apt-get install -y docker-ce-cli docker-ce

RUN chmod u+wx /etc/sudoers
COPY --chown=jenkins:jenkins plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt

# USER jenkins
# RUN jenkins-plugin-cli --plugins "blueocean:latest docker-workflow:latest"